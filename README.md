# Cafe Manager

이 프로젝트는 Spring Boot 기반의 카페 관리 웹 애플리케이션입니다. 서버는 Java/Spring Boot로 구현되어 있으며, 클라이언트는 Thymeleaf 템플릿과 React 프론트엔드를 함께 사용합니다.

## 주요 기능

- Spring Boot 웹 애플리케이션
- Thymeleaf 기반 서버 사이드 렌더링
- React 프런트엔드(`src/main/ront`) 빌드 통합
- Spring Security 로그인/회원가입
- JDBC 및 JPA 데이터 접근
- MariaDB/MySQL 연동
- WebSocket 기능 지원
- QR 코드 생성 및 처리(ZXing 라이브러리 사용)

## 주요 기술 스택

- Java 17
- Spring Boot 3.3.2
- Spring Web
- Spring Data JPA
- Spring Security
- Spring WebSocket
- Thymeleaf
- MariaDB/MySQL JDBC
- ZXing
- Lombok
- React 18

## 프로젝트 구조

- `pom.xml` - Maven 빌드 설정
- `src/main/java/myapp` - 애플리케이션 소스 코드
  - `config/` - 설정 관련 클래스
  - `controller/` - 웹 요청 처리 컨트롤러
  - `entity/` - 데이터베이스 엔티티
  - `repository/` - JPA 리포지토리
  - `security/` - Spring Security 설정
  - `service/` - 비즈니스 로직 서비스
- `src/main/resources` - 리소스 파일
  - `application.properties`, `application.yml` - 설정 파일
  - `templates/` - Thymeleaf 템플릿
  - `static/` - 정적 리소스(CSS, 이미지, JS, QR 코드 등)
- `src/main/ront` - React 프런트엔드 스캐폴딩 및 빌드
- `target/` - Maven 빌드 출력 디렉터리

## 실행 방법

### 1. Java 버전

현재 `pom.xml`은 Java 17을 사용하도록 설정되어 있습니다. 시스템에 JDK 17 이상이 설치되어 있어야 합니다.

### 2. Maven 빌드

Windows:
```powershell
mvnw.cmd clean package
```

Unix/macOS:
```bash
./mvnw clean package
```

### 3. 애플리케이션 실행

Windows:
```powershell
mvnw.cmd spring-boot:run
```

Unix/macOS:
```bash
./mvnw spring-boot:run
```

### 4. React 프런트엔드 빌드

React 프런트엔드는 `src/main/ront` 폴더에 있습니다. 별도로 빌드하려면:

```bash
cd src/main/ront
npm install
npm run build
```

빌드 스크립트는 빌드 후 필요한 파일을 복사하는 커스텀 스크립트를 포함합니다.

## 주요 진입점

- `src/main/java/myapp/ManagementApplication.java` - Spring Boot 애플리케이션 메인 클래스

## 참고

- `src/main/resources/templates` 폴더에 로그인, 회원가입, 관리자 페이지 등 Thymeleaf 템플릿이 포함되어 있습니다.
- `src/main/resources/static` 폴더에는 CSS, 이미지, JavaScript, QR 코드 자원이 위치합니다.

## 권장 사항

- Java 최신 LTS 버전으로 업그레이드 시 `pom.xml`의 `java.version`과 `maven-compiler-plugin` 설정을 함께 업데이트해야 합니다.
- 데이터베이스 연결 정보는 `application.yml` 또는 `application.properties`에 설정되어 있습니다.
