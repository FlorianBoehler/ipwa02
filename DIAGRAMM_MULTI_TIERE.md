```mermaid
graph TD
    A[Presentation Layer] -->|depends on| B[Application Layer]
    B -->|depends on| C[Data Layer]
    
    subgraph "Presentation Layer"
        D[Client 1]
        E[Client 2]
        F[Client ...]
    end
    
    subgraph "Application Layer"
        G[Java Server - Jakarta EE]
        H[Service 1]
        I[Service 2]
        J[Service ...]
    end
    
    subgraph "Data Layer"
        K[(Database)]
    end
    
    D -.-> G
    E -.-> G
    F -.-> G
    G --- H
    G --- I
    G --- J
    G --> K