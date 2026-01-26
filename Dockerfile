# ใช้ Image พื้นฐานเป็น Java 17 (หรือเวอร์ชันที่คุณใช้)
FROM openjdk:21-jdk-slim

# ก็อปปี้ไฟล์โปรเจกต์เข้าไปใน Container
COPY . /app
WORKDIR /app

# สั่ง Build ด้วย Maven Wrapper
RUN ./mvnw clean package -DskipTests

# บอกว่าให้รันไฟล์ .jar ที่ได้จากการ Build
ENTRYPOINT ["java", "-jar", "target/petshop-0.0.1-SNAPSHOT.jar"]
# *หมายเหตุ: ต้องแก้ชื่อไฟล์ jar ให้ตรงกับที่ระบุใน pom.xml (ตรง <artifactId> และ <version>)