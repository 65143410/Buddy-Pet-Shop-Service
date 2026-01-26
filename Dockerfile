# ใช้ Base Image เป็น Java 21 ตามที่ระบุใน pom.xml
FROM eclipse-temurin:21-jdk-jammy

# ตั้งค่า Working Directory
WORKDIR /app

# ก็อปปี้ไฟล์ทั้งหมดในโปรเจกต์เข้าไปใน Docker
COPY . .

# ให้สิทธิ์ไฟล์ mvnw สามารถรันได้ (กันเหนียวสำหรับคนใช้ Windows)
RUN chmod +x mvnw

# สั่ง Build โปรเจกต์ (ข้าม Test เพื่อความเร็ว)
RUN ./mvnw clean package -DskipTests

# คำสั่งรันเมื่อ Deploy เสร็จ
# ชื่อไฟล์ต้องตรงกับ artifactId และ version ใน pom.xml
ENTRYPOINT ["java", "-jar", "target/petshop-0.0.1-SNAPSHOT.jar"]