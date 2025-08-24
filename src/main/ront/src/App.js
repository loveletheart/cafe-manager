import React from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link
} from 'react-router-dom';
import CompletedOrders from './admin/CompletedOrders';

function App() {
  return (
    <Router>
      <div className="App p-8 bg-gray-100 min-h-screen">
        <nav className="mb-8">
          <ul className="flex justify-center space-x-4">
            <li>
              <Link to="/admin/completed" className="text-blue-500 hover:text-blue-700 font-semibold transition-colors duration-200">
                완료된 주문 (React)
              </Link>
            </li>
            <li>
              <a href="/admin/ODS" className="text-green-500 hover:text-green-700 font-semibold transition-colors duration-200">
                ODS 페이지 (HTML)
              </a>
            </li>
          </ul>
        </nav>
        <div className="bg-white rounded-lg shadow-md p-6">
          <Routes>
            <Route path="/admin/completed" element={<CompletedOrders />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
