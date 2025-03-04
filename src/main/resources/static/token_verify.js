window.addEventListener('DOMContentLoaded', (event) => {
  const token = sessionStorage.getItem("Authorization");

  if (token) {
    // 토큰이 있으면, 인증된 요청을 서버에 보냄
    fetch("/api/auth/verify", {
      method: "POST",
      headers: {
        "Authorization": token
      }
    })
    .then(response => {
      if (!response.ok) {
        return response.json().then(errorData => {
          console.log(errorData);
          throw new Error(errorData.message);
        });
      }
      return response.json();
    })
    .then(data => {
      console.log("response data:", data);
    })
    .catch(error => {
      console.error('Error:', error);
      alert(error.message);
    });
  } else {
    // 토큰이 없으면 로그인 페이지로 리다이렉트
    console.log("Token not found. Redirecting to login page.");
    alert("로그인 해주세요.");
    window.location.href = '/login.html';
  }
});