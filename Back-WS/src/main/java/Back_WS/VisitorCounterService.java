package Back_WS;

import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Data
@Service
public class VisitorCounterService {
    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    @Getter
    private volatile int count;
    private volatile boolean running = false;

    public VisitorCounterService() {
        // Démarre le compteur initial entre 10 et 50
        this.count = 10 + random.nextInt(41);
        startSimulation();
    }


    private void startSimulation() {
        if (running) return;
        running = true;

        scheduler.scheduleAtFixedRate(() -> {
            // 70% de chance de changement
            if (random.nextDouble() < 0.7) {
                int delta = random.nextInt(9) - 3; // -3 à +5
                if (delta < -3) delta = -3;
                if (delta > 5) delta = 5;
                count = Math.max(0, Math.min(200, count + delta));
            }
        }, 0, 2 + random.nextInt(2), TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
        running = false;
    }
}
