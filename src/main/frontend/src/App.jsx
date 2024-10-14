import React, { useEffect, useState } from 'react';
import Home from './components/Home'; // 홈 화면 컴포넌트

const App = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            // 백엔드에서 토큰 검증 API 호출
            fetch('http://localhost:8080/api/auth/validate', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            })
                .then(response => {
                    if (response.ok) {
                        setIsLoggedIn(true); // 토큰이 유효하면 로그인 상태로 설정
                    } else {
                        setIsLoggedIn(false); // 토큰이 유효하지 않으면 로그인 상태 해제
                    }
                })
                .catch(() => {
                    setIsLoggedIn(false); // 요청 실패 시 로그인 상태 해제
                });
        } else {
            setIsLoggedIn(false); // 토큰이 없으면 로그인 상태 해제
        }
    }, []);

    const handleLoginClick = () => {
        // 구글 로그인 페이지로 이동
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    };

    // 구글 로그인 후 JWT 토큰을 백엔드에서 받아와 localStorage에 저장하는 부분
    useEffect(() => {
        const queryParams = new URLSearchParams(window.location.search);
        const token = queryParams.get('token');
        if (token) {
            // JWT 토큰을 localStorage에 저장
            localStorage.setItem('token', token);
            setIsLoggedIn(true); // 로그인 상태 설정
        }
    }, []);

    return (
        <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <Home isLoggedIn={isLoggedIn} handleLoginClick={handleLoginClick} />
        </div>
    );
};

export default App;
