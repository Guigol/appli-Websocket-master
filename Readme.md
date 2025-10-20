# Notes :


```
Back_WS/
 ├── src/
 │   ├── main/
 │   │   ├── java/
 │   │   │   └── Back_WS/
 │   │   │       ├── BackWsApplication.java
 │   │   │       ├── CounterController.java
 │   │   │       └── VisitorCounterService.java
 │   │   └── resources/
 │   │       └── application.properties
 └── pom.xml   ← (classique Spring Boot)
 
```

| Aspect                    | Polling                      | SSE                         |
| ------------------------- | ---------------------------- | --------------------------- |
| Type                      | Requêtes périodiques         | Connexion persistante       |
| Fréquence réseau          | Forte (1 req/2s)             | Très faible (1 flux unique) |
| Latence                   | Dépend du cycle              | Instantanée                 |
| Charge serveur            | Haute si beaucoup de clients | Très faible                 |
| Fonctionne en parallèle ? | ✅ Oui                        | ✅ Oui                       |


## Observations :

- En mode `POLLING`, on constate qu'il y a presque une requête par seconde (0.59), 18 headers envoyés et 31 requêtes HTTP sans aucun changement de donnée.
- Tandis qu'en mode `SSE`, 1 seule requête HTTP et un seul header persistent avec la même charge d'utilisateurs connectés (197).
- Optimisation non négligeable.
