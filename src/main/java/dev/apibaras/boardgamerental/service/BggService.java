package dev.apibaras.boardgamerental.service;

import dev.apibaras.boardgamerental.model.boardgame.BoardGameSearchResponse;
import dev.apibaras.boardgamerental.model.boardgame.BggThingDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class BggService {

    @Value("${bgg.api.token:}")
    private String bggApiToken;

    private HttpClient httpClient;
    private DocumentBuilder documentBuilder;

    private static final String SEARCH_URL = "https://boardgamegeek.com/xmlapi2/search";
    private static final String THING_URL = "https://boardgamegeek.com/xmlapi2/thing";

    @PostConstruct
    public void init() throws Exception {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        this.documentBuilder = factory.newDocumentBuilder();
    }

    // 1) Search by name (like SQL LIKE) and return up to 10 results with id, name, image, thumbnail
    public Set<BoardGameSearchResponse> searchBoardGames(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptySet();
        }
        ensureTokenPresent();

        try {
            String url = SEARCH_URL + "?query=" + uriEncode(query) + "&type=boardgame";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .header("Accept", "application/xml")
                    .header("Authorization", "Bearer " + bggApiToken)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new RuntimeException("BGG search failed: " + response.statusCode());
            }

            Document doc = documentBuilder.parse(new ByteArrayInputStream(response.body().getBytes(StandardCharsets.UTF_8)));
            NodeList items = doc.getElementsByTagName("item");
            List<String> ids = new ArrayList<>();

            for (int i = 0; i < items.getLength() && ids.size() < 10; i++) {
                Node node = items.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) continue;
                Element el = (Element) node;
                String id = el.getAttribute("id");
                if (id != null && !id.isBlank()) {
                    ids.add(id);
                }
            }

            if (ids.isEmpty()) return Collections.emptySet();

            // Fetch full thing data for these ids in one request
            String idsParam = String.join(",", ids);
            String thingUrl = THING_URL + "?id=" + idsParam + "&stats=0";
            HttpRequest thingRequest = HttpRequest.newBuilder()
                    .uri(URI.create(thingUrl))
                    .timeout(Duration.ofSeconds(20))
                    .header("Accept", "application/xml")
                    .header("Authorization", "Bearer " + bggApiToken)
                    .GET()
                    .build();

            HttpResponse<String> thingResponse = httpClient.send(thingRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (thingResponse.statusCode() != 200) {
                throw new RuntimeException("BGG thing request failed: " + thingResponse.statusCode());
            }

            Document thingsDoc = documentBuilder.parse(new ByteArrayInputStream(thingResponse.body().getBytes(StandardCharsets.UTF_8)));
            NodeList thingItems = thingsDoc.getElementsByTagName("item");

            List<BoardGameSearchResponse> results = new ArrayList<>();
            for (int i = 0; i < thingItems.getLength(); i++) {
                Node n = thingItems.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE) continue;
                Element itemEl = (Element) n;
                String idStr = itemEl.getAttribute("id");
                long id = parseLongOrZero(idStr);

                String name = null;
                NodeList nameNodes = itemEl.getElementsByTagName("name");
                for (int j = 0; j < nameNodes.getLength(); j++) {
                    Node nm = nameNodes.item(j);
                    if (nm.getNodeType() != Node.ELEMENT_NODE) continue;
                    Element nameEl = (Element) nm;
                    String typeAttr = nameEl.getAttribute("type");
                    if (typeAttr == null || "primary".equals(typeAttr) || nameNodes.getLength() == 1) {
                        name = nameEl.getAttribute("value");
                        break;
                    }
                }

                String image = textContentOfTag(itemEl, "image");
                String thumbnail = textContentOfTag(itemEl, "thumbnail");

                BoardGameSearchResponse bg = new BoardGameSearchResponse();
                bg.setBoardGameId(id);
                bg.setName(name == null ? "" : name);
                bg.setImage(image);
                bg.setThumbnail(thumbnail);
                bg.setQuantity(1);
                bg.setQuantityAvailable(1);

                results.add(bg);
            }

            // Preserve original ordering by ids list
            Map<Long, BoardGameSearchResponse> mapById = results.stream()
                    .collect(Collectors.toMap(BoardGameSearchResponse::getBoardGameId, r -> r));

            List<BoardGameSearchResponse> ordered = ids.stream()
                    .map(s -> mapById.getOrDefault(parseLongOrZero(s), null))
                    .filter(Objects::nonNull)
                    .toList();

            return new LinkedHashSet<>(ordered);

        } catch (Exception e) {
            throw new RuntimeException("Failed to search BGG: " + e.getMessage(), e);
        }
    }

    // 2) Get thing by id and return a DTO with details
    public BggThingDto getThingById(long id) {
        ensureTokenPresent();
        try {
            String thingUrl = THING_URL + "?id=" + id + "&stats=1";
            HttpRequest thingRequest = HttpRequest.newBuilder()
                    .uri(URI.create(thingUrl))
                    .timeout(Duration.ofSeconds(20))
                    .header("Accept", "application/xml")
                    .header("Authorization", "Bearer " + bggApiToken)
                    .GET()
                    .build();

            HttpResponse<String> thingResponse = httpClient.send(thingRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (thingResponse.statusCode() != 200) {
                throw new RuntimeException("BGG thing request failed: " + thingResponse.statusCode());
            }

            Document doc = documentBuilder.parse(new ByteArrayInputStream(thingResponse.body().getBytes(StandardCharsets.UTF_8)));
            NodeList items = doc.getElementsByTagName("item");
            if (items.getLength() == 0) return null;
            Element itemEl = (Element) items.item(0);

            String name = null;
            NodeList nameNodes = itemEl.getElementsByTagName("name");
            for (int j = 0; j < nameNodes.getLength(); j++) {
                Node nm = nameNodes.item(j);
                if (nm.getNodeType() != Node.ELEMENT_NODE) continue;
                Element nameEl = (Element) nm;
                String typeAttr = nameEl.getAttribute("type");
                if (typeAttr == null || "primary".equals(typeAttr) || nameNodes.getLength() == 1) {
                    name = nameEl.getAttribute("value");
                    break;
                }
            }

            String image = textContentOfTag(itemEl, "image");
            String thumbnail = textContentOfTag(itemEl, "thumbnail");
            String description = textContentOfTag(itemEl, "description");

            int yearPublished = parseIntOrZero(getChildAttribute(itemEl, "yearpublished", "value"));
            int minPlayers = parseIntOrZero(getChildAttribute(itemEl, "minplayers", "value"));
            int maxPlayers = parseIntOrZero(getChildAttribute(itemEl, "maxplayers", "value"));
            int playingTime = parseIntOrZero(getChildAttribute(itemEl, "playingtime", "value"));

            // publisher: link elements with type=boardgamepublisher
            String publisher = null;
            NodeList linkNodes = itemEl.getElementsByTagName("link");
            for (int i = 0; i < linkNodes.getLength(); i++) {
                Node ln = linkNodes.item(i);
                if (ln.getNodeType() != Node.ELEMENT_NODE) continue;
                Element linkEl = (Element) ln;
                String type = linkEl.getAttribute("type");
                if ("boardgamepublisher".equals(type)) {
                    publisher = linkEl.getAttribute("value");
                    break;
                }
            }

            // average rating if present under statistics/ratings/average
            double averageRating = 0.0;
            NodeList statsNodes = doc.getElementsByTagName("average");
            if (statsNodes.getLength() > 0) {
                String avg = ((Element) statsNodes.item(0)).getAttribute("value");
                try { averageRating = Double.parseDouble(avg); } catch (Exception ignore) {}
            }

            BggThingDto dto = new BggThingDto();
            dto.setId(id);
            dto.setName(name);
            dto.setImage(image);
            dto.setThumbnail(thumbnail);
            dto.setDescription(description);
            dto.setPublisher(publisher);
            dto.setYearPublished(yearPublished);
            dto.setMinPlayers(minPlayers);
            dto.setMaxPlayers(maxPlayers);
            dto.setPlayingTime(playingTime);
            dto.setAverageRating(averageRating);

            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch thing from BGG: " + e.getMessage(), e);
        }
    }

    // helpers
    private void ensureTokenPresent() {
        if (bggApiToken == null || bggApiToken.isBlank()) {
            throw new IllegalStateException("BGG API bearer token not configured (property bgg.api.token)");
        }
    }

    private static String uriEncode(String s) {
        return s.replace(" ", "+");
    }

    private static String textContentOfTag(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) return null;
        Node n = list.item(0);
        return n == null ? null : n.getTextContent();
    }

    private static String getChildAttribute(Element parent, String tagName, String attr) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) return null;
        Node n = list.item(0);
        if (n.getNodeType() != Node.ELEMENT_NODE) return null;
        return ((Element) n).getAttribute(attr);
    }

    private static long parseLongOrZero(String s) {
        if (s == null) return 0L;
        try { return Long.parseLong(s); } catch (Exception e) { return 0L; }
    }

    private static int parseIntOrZero(String s) {
        if (s == null) return 0;
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
}
