import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { format } from 'date-fns';

function CompletedOrders() {
  const [completedOrders, setCompletedOrders] = useState({}); // 그룹화된 주문을 저장할 객체
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

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

  useEffect(() => {
    // 컴포넌트가 처음 렌더링될 때 '준비완료' 주문 목록을 가져옵니다.
    fetchCompletedOrders();

    // 5초마다 주문 목록을 새로고침합니다.
    const intervalId = setInterval(fetchCompletedOrders, 5000);

    // 컴포넌트가 언마운트될 때 인터벌을 정리합니다.
    return () => clearInterval(intervalId);
  }, []);

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
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

export default CompletedOrders;