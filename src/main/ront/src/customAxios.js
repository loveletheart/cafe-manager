import axios from 'axios';

const customAxios = (url, callback) => {
  axios.get(url)
    .then(response => {
      // 요청이 성공했을 경우
      callback(response.data);
    })
    .catch(error => {
      // 요청이 실패했을 경우
      console.error("요청 실패:", error);
      callback('IP 주소를 가져오지 못했습니다.'); // 실패 시 사용자에게 알림
    });
};

export default customAxios;