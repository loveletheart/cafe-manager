const fs = require('fs');
const path = require('path');

// 빌드 폴더의 경로 설정
const buildDir = path.join(__dirname, '..', 'build', 'static', 'js');
const sourceFilePattern = /^main\..*\.js$/;
const destFileName = 'completed-bundle.js';
const destDir = path.join(__dirname, '..', '..', 'resources', 'static', 'js');

try {
  // 1. 빌드 디렉토리에서 main.<hash>.js 파일을 찾습니다.
  const files = fs.readdirSync(buildDir);
  const sourceFile = files.find(file => sourceFilePattern.test(file));

  if (!sourceFile) {
    throw new Error('main.<hash>.js 파일을 찾을 수 없습니다.');
  }

  const sourcePath = path.join(buildDir, sourceFile);
  const destPath = path.join(destDir, destFileName);

  // 2. 목적지 디렉토리가 없으면 생성합니다.
  if (!fs.existsSync(destDir)) {
    fs.mkdirSync(destDir, { recursive: true });
  }

  // 3. 빌드된 파일을 복사합니다.
  fs.copyFileSync(sourcePath, destPath);
  console.log(`✅ 파일 복사 완료: ${destPath}`);

} catch (error) {
  console.error('❌ 스크립트 실행 중 오류가 발생했습니다:', error.message);
  process.exit(1);
}
