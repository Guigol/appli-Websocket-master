package Back_WS;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Active CORS
public class CounterController {

    // TODO : Implémenter la simulation du compteur
    // - Service/composant qui gère l'état et la simulation

    private final VisitorCounterService counterService;

    public CounterController(VisitorCounterService counterService) {
        this.counterService = counterService;
    }


    // ========================================
    // ENDPOINT POLLING : GET /api/count
    // ========================================
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCount() {
        // TODO : Récupérer le compteur depuis votre logique
        int count = counterService.getCount();
        return ResponseEntity.ok(Map.of(
                "count", count,
                "timestamp", Instant.now().toString()
        ));
    }

    // ========================================
    // ENDPOINT SSE : GET /api/stream
    // ========================================
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(0L);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            int lastCount = -1;
            try {
                while (true) {
                    int currentCount = counterService.getCount();

                    if (currentCount != lastCount) {
                        lastCount = currentCount;
                        Map<String, Object> data = Map.of(
                                "count", currentCount,
                                "timestamp", Instant.now().toString()
                        );
                        try {
                            emitter.send(SseEmitter.event().data(data));
                        } catch (IllegalStateException e) {
                            // Le client a fermé la connexion
                            System.out.println("⚠️ Connexion SSE fermée par le client.");
                            break; // Sortir de la boucle proprement
                        }
                    }

                    Thread.sleep(2000 + (int) (Math.random() * 1000));
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("❌ Erreur SSE : " + e.getMessage());
            } finally {
                emitter.complete();
                executor.shutdown();
            }
        });

        emitter.onCompletion(() -> {
            System.out.println("✅ SSE terminé proprement.");
            executor.shutdown();
        });
        emitter.onTimeout(() -> {
            System.out.println("⏰ SSE timeout.");
            emitter.complete();
            executor.shutdown();
        });

        return emitter;
    }

}