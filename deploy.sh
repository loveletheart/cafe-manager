#!/bin/bash

echo "🚀 배포 시작: $(date)"

# 1. 기존 프로세스 종료
PID=$(pgrep -f 'management-0.0.1-SNAPSHOT.jar')
if [ -n "$PID" ]; then
  echo "🛑 기존 앱 종료 중 (PID: $PID)..."
  sudo kill -15 "$PID" # 우아한 종료 (SIGTERM) 시도
  sleep 10 # 종료 대기 시간

  if kill -0 "$PID" 2>/dev/null; then
    echo "⚠️ 프로세스 $PID가 종료되지 않아 강제 종료합니다."
    sudo kill -9 "$PID"
  else
    echo "✅ 프로세스 $PID 종료됨"
  fi
else
  echo "ℹ️ 실행 중인 앱이 없습니다."
fi

# 2. target 디렉토리 삭제
echo "🧹 기존 빌드 파일 삭제 중..."
rm -rf target/
echo "✅ 삭제 완료"

# 3. 프로젝트 빌드
echo "🔨 프로젝트 빌드 중..."
./mvnw clean package -DskipTests || { echo "❌ 프로젝트 빌드 실패."; exit 1; }

# JAR 파일이 성공적으로 생성되었는지 확인
JAR_PATH="./target/management-0.0.1-SNAPSHOT.jar" # 실제 JAR 경로 확인 필요
if [ ! -f "$JAR_PATH" ]; then
  echo "❌ 빌드된 JAR 파일(${JAR_PATH})을 찾을 수 없습니다. 빌드 실패 확인."
  exit 1
fi

# 4. JAR 실행
echo "🚀 JAR 실행 중..."
# app.log 파일은 Jenkins 워크스페이스(/var/lib/jenkins/workspace/cafe-manager/app.log)에 생성됩니다.
nohup sudo java -jar "$JAR_PATH" > app.log 2>&1 &

NEW_PID=$!
echo "✅ 새 애플리케이션이 PID: $NEW_PID 로 시작되었습니다."
echo "로그 파일: ./app.log (Jenkins 워크스페이스 내)"

sleep 7 # 애플리케이션 시작 대기 (조정 가능)

NEW_APP_STATUS=$(pgrep -f 'management-0.0.1-SNAPSHOT.jar')
if [ -n "$NEW_APP_STATUS" ] && [ "$NEW_APP_STATUS" != "$PID" ]; then
  echo "--- 새 애플리케이션이 성공적으로 시작되었습니다. (PID: $NEW_APP_STATUS) ---"
else
  echo "--- ❌ 새 애플리케이션 시작 실패 또는 PID 불일치. 로그를 확인하세요. ---"
  exit 1
fi

echo "✅ 배포 완료: $(date)"