// src/components/main.jsx
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Main = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            axios.post('https://fairytalegenerator.kro.kr/api/auth/validate', {}, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            })
                .then(response => {
                    if (response.status === 200) {
                        setIsLoggedIn(true);
                    } else {
                        setIsLoggedIn(false);
                    }
                })
                .catch(() => {
                    setIsLoggedIn(false);
                });
        } else {
            setIsLoggedIn(false);
        }
    }, []);

    const handleLoginClick = () => {
        window.location.href = 'https://fairytalegenerator.kro.kr/oauth2/authorization/google';
    };

    useEffect(() => {
        const queryParams = new URLSearchParams(window.location.search);
        const token = queryParams.get('token');
        if (token) {
            localStorage.setItem('token', token);
            setIsLoggedIn(true);
        }
    }, []);

    return (
        <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <h1>환영합니다! 홈 화면입니다.</h1>
            {isLoggedIn ? (
                <p>구글 소셜 로그인이 성공했습니다.</p>
            ) : (
                <div>
                    <p>로그인이 필요합니다.</p>
                    <button onClick={handleLoginClick}>구글로 로그인</button>
                </div>
            )}
        </div>
    );
};

export default Main;
