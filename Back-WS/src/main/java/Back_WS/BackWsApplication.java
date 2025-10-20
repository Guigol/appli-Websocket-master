package Back_WS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackWsApplication.class, args);
		System.out.println("🚀 Serveur démarré sur http://localhost:8081");
		System.out.println("   → Endpoint Polling : GET /api/count");
		System.out.println("   → Endpoint SSE :     GET /api/stream");
	}
}
