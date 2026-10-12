# Usamos una imagen oficial de Java 21 (compatible con el código de Jeiferson)
FROM eclipse-temurin:21-jre-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar los archivos compilados (.class) y las librerías
COPY *.class ./
COPY *.java ./
COPY lib/ ./lib/
COPY config/ ./config/
COPY controllers/ ./controllers/
COPY models/ ./models/
COPY routes/ ./routes/
COPY utils/ ./utils/
COPY agroconnect.db ./

# Exponer el puerto que usa el servidor Java
EXPOSE 3000

# Comando para iniciar el servidor
# En Linux el separador del classpath es ":" (no ";" como en Windows)
CMD ["java", "-cp", ".:lib/*", "Servidor"]
