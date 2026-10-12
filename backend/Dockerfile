# Usamos Java 21 (compatible con el código del proyecto)
FROM eclipse-temurin:25-jre

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar todo desde la carpeta backend/ del repo
# (el build context es la raíz del repo, por eso el prefijo backend/)
COPY backend/*.class ./
COPY backend/lib/ ./lib/
COPY backend/config/ ./config/
COPY backend/controllers/ ./controllers/
COPY backend/models/ ./models/
COPY backend/routes/ ./routes/
COPY backend/utils/ ./utils/
COPY backend/agroconnect.db ./
COPY backend/.env ./


# Exponer el puerto del servidor Java
EXPOSE 3000

# En Linux el separador del classpath es ":" (no ";" como en Windows)
CMD ["java", "-cp", ".:lib/*", "Servidor"]
