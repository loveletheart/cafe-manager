import React, { useState, useEffect } from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link
} from 'react-router-dom';
import axios from 'axios';

function App() {
  return (
    <Router>
      <div className="App">
        <nav>
          <ul>
            <li>
              <Link to="/">홈</Link>
            </li>
            <li>
              <Link to="/about">소개</Link>
            </li>
            <li>
              <Link to="/users">사용자</Link>
            </li>
          </ul>
        </nav>
        <Routes>
          {/* 'Route' 컴포넌트는 'element' 속성을 사용하여 렌더링할 컴포넌트를 지정합니다. */}
          <Route path="/about" element={<About />} />
          <Route path="/users" element={<Users />} />
          <Route path="/" element={<Home />} />
        </Routes>
      </div>
    </Router>
  );
}

function Home() {
  // IP주소 변수 선언
  const [ip, setIp] = useState('');

  // IP주소 값을 설정합니다.
  function callback(data) {
    setIp(data);
  }

  // 첫번째 렌더링을 다 마친 후 실행합니다.
  useEffect(
    () => {
      // customAxios 대신 axios를 직접 사용합니다.
      // 클라이언트의 IP주소를 알아내는 백엔드의 함수를 호출합니다.
      axios.get('/admin/ip').then(response => {
        callback(response.data);
      }).catch(error => {
        console.error("Error fetching IP:", error);
      });
    }, []
  );

  return (
    <header className="App-header">
      이 기기의 IP주소는 {ip}입니다.
    </header>
  );
}

function About() {
  return (
    <div>
      <hr />
      <h2>소개 페이지</h2>
    </div>
  );
}

function Users() {
  return (
    <div>
      <hr />
      <h2>사용자 페이지</h2>
    </div>
  );
}

export default App;