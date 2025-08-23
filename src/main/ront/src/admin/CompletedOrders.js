import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { format } from 'date-fns';

function CompletedOrders() {
  // 그룹화된 주문을 저장할 객체
  const [completedOrders, setCompletedOrders] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');

  // '준비완료' 상태의 주문 목록을 가져오는 함수
  const fetchCompletedOrders = () => {
    setLoading(true);
    // '/admin/complete' API를 호출하여 '준비완료' 상태의 주문 목록을 가져옵니다.
    axios.get('/admin/complete')
      .then(response => {
        // 서버에서 받은 데이터를 그룹 ID별로 묶습니다.
        const grouped = response.data.reduce((acc, order) => {
          const groupId = order.orderGroupId;
          if (!acc[groupId]) {
            acc[groupId] = {
              orderGroupId: groupId,
              items: [],
              orderTime: order.orderTime,
            };
          }
          acc[groupId].items.push(order);
          return acc;
        }, {});
        setCompletedOrders(grouped);
        setLoading(false);
      })
      .catch(err => {
        console.error("Error fetching completed orders:", err);
        setError("완료된 주문 목록을 불러오는 데 실패했습니다.");
        setLoading(false);
      });
  };

  // 주문을 다시 '주문완료' 상태로 되돌리는 함수
  const handleRevertOrder = (orderGroupId) => {
    // 주문 되돌리기 API 호출
    axios.post(`/admin/revert/${orderGroupId}`)
      .then(response => {
        // 성공적으로 되돌려졌다면, 목록을 새로고침합니다.
        console.log("Order reverted successfully:", response.data);
        fetchCompletedOrders();
        setModalMessage("주문이 성공적으로 되돌려졌습니다.");
        setShowModal(true);
      })
      .catch(err => {
        console.error("Error reverting order:", err);
        setModalMessage("주문 되돌리기 실패: " + (err.response?.data || err.message));
        setShowModal(true);
      });
  };

  useEffect(() => {
    // 컴포넌트가 마운트될 때 CSRF 토큰을 가져와 axios 기본 헤더에 설정
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    if (csrfToken && csrfHeader) {
      axios.defaults.headers.common[csrfHeader] = csrfToken;
    }

    // 초기 주문 목록 가져오기
    fetchCompletedOrders();
  }, []);

  const closeModal = () => {
    setShowModal(false);
    setModalMessage('');
  };

  if (loading) return <div className="text-center p-4">완료된 주문 목록을 불러오는 중...</div>;
  if (error) return <div className="text-center p-4 text-red-500">{error}</div>;

  const orderGroupIds = Object.keys(completedOrders);

  return (
    <div className="p-4 bg-gray-50 rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-4 text-center">준비 완료된 주문 내역</h2>
      {orderGroupIds.length === 0 ? (
        <p className="text-center text-gray-500">완료된 주문 내역이 없습니다.</p>
      ) : (
        <div className="order-container flex flex-wrap gap-5 p-5">
          {orderGroupIds.map(groupId => {
            const orderGroup = completedOrders[groupId];
            const displayTime = orderGroup.orderTime ? format(new Date(`2000-01-01T${orderGroup.orderTime}`), 'HH:mm:ss') : '시간 정보 없음';

            return (
              <div
                key={groupId}
                className="order-card w-[calc(100%/5-1rem)] min-h-[260px] border border-gray-200 shadow-lg rounded-xl bg-white p-5 relative pb-16 flex flex-col justify-between transition-transform duration-200 hover:transform hover:-translate-y-1"
              >
                <div className="order-header flex justify-between items-center mb-4 border-b border-dashed border-gray-300 pb-2">
                  <span className="order-title text-lg font-bold text-gray-800">주문테이블: {orderGroup.items[0].userId}</span>
                </div>
                <div className="menu-items-scroll max-h-[120px] overflow-y-auto mb-4 flex-grow">
                  {orderGroup.items.map(item => (
                    <div key={item.orderId} className="order-info text-sm my-1 p-2 rounded-md transition-colors duration-200 bg-gray-50 border border-gray-200 hover:bg-gray-200">
                      <p className="font-semibold">{item.menuName} - {item.quantity}개</p>
                      {item.temperature || item.beanType || item.cupType || item.syrup ? (
                        <div className="order-options text-xs text-gray-500 ml-5 mt-[-3px] leading-tight">
                          {item.temperature && <span>{item.temperature}</span>}
                          {item.beanType && <span>{item.beanType}</span>}
                          {item.cupType && <span>{item.cupType}</span>}
                          {item.syrup && <span>{item.syrup}</span>}
                        </div>
                      ) : null}
                    </div>
                  ))}
                </div>
                <div className="bottom-controls absolute bottom-4 left-5 right-5 flex justify-between items-center pt-2 border-t border-gray-200">
                  <span className="order-time text-sm text-gray-500">완료 시간: {displayTime}</span>
                  <button
                    type="button"
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline transition-colors duration-200"
                    onClick={() => handleRevertOrder(orderGroup.orderGroupId)}
                  >
                    주문 완료 되돌리기
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      )}
      {showModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex justify-center items-center">
          <div className="relative p-5 border w-96 shadow-lg rounded-md bg-white text-center">
            <h3 className="text-xl font-bold mb-4">알림</h3>
            <p className="text-sm text-gray-600 mb-6">{modalMessage}</p>
            <div className="flex justify-center">
              <button
                onClick={closeModal}
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
              >
                확인
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default CompletedOrders;
