import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Main = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        // URL에서 토큰을 추출해 저장하고, 환경에 따라 리디렉션
        const params = new URLSearchParams(window.location.search);
        const token = params.get('token');

        if (token) {
            localStorage.setItem('authToken', token); // 토큰을 로컬 스토리지에 저장
            // 환경에 따라 다른 경로로 리디렉션
            if (window.location.hostname === 'localhost') {
                window.location.replace('http://localhost:3000/home'); // 로컬 환경 리디렉션
            } else {
                window.location.replace('https://fairytalegenerator.kro.kr/home'); // 서버 환경 리디렉션
            }
            return; // 이후 코드 실행 방지
        }

        // 기존 로그인 검증 코드
        const authToken = localStorage.getItem('authToken');
        if (authToken) {
            axios.post('https://fairytalegenerator.kro.kr/api/auth/validate', {}, {
                headers: {
                    'Authorization': `Bearer ${authToken}`,
                },
            })
                .then(response => {
                    if (response.status === 200) {
                        setIsLoggedIn(true);
                    } else {
                        setIsLoggedIn(false);
                        localStorage.removeItem('authToken'); // 유효하지 않으면 토큰 제거
                    }
                })
                .catch(() => {
                    setIsLoggedIn(false);
                    localStorage.removeItem('authToken');
                });
        }
    }, []);

    const handleLoginClick = () => {
        const popup = window.open('https://fairytalegenerator.kro.kr/oauth2/authorization/google', '_blank', 'width=500,height=600');

        const handleMessage = (event) => {
            if (event.origin === 'http://localhost:3000' && event.data.token) {
                localStorage.setItem('authToken', event.data.token);
                setIsLoggedIn(true);
                navigate('/home');
                popup.close();
                window.removeEventListener('message', handleMessage); // 이벤트 리스너 제거
            }
        };

        window.addEventListener('message', handleMessage, false);
    };

    return (
        <div style={styles.container}>
            <h1 style={styles.title}>🧚‍♀️ 환영합니다! 동화의 나라에 오신 것을 환영합니다 🧚‍♂️</h1>
            {isLoggedIn ? (
                <p style={styles.message}>구글 소셜 로그인이 성공했습니다.</p>
            ) : (
                <div style={styles.loginContainer}>
                    <p style={styles.message}>로그인이 필요합니다. 모험을 시작하려면 로그인해 주세요.</p>
                    <button onClick={handleLoginClick} style={styles.loginButton}>구글로 로그인</button>
                </div>
            )}
        </div>
    );
};

const styles = {
    container: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        textAlign: 'center',
        marginTop: '50px',
        backgroundColor: '#FFFAF0',
        minHeight: '100vh',
        padding: '20px',
    },
    title: {
        fontSize: '2.5rem',
        color: '#6B8E23',
        fontFamily: '"Comic Sans MS", cursive, sans-serif',
        marginBottom: '20px',
    },
    message: {
        fontSize: '1.2rem',
        color: '#4B0082',
        marginBottom: '20px',
    },
    loginButton: {
        backgroundColor: '#FFD700',
        color: '#4B0082',
        padding: '10px 20px',
        border: 'none',
        borderRadius: '25px',
        fontSize: '1.1rem',
        cursor: 'pointer',
        fontFamily: '"Comic Sans MS", cursive, sans-serif',
        transition: 'transform 0.3s, background-color 0.3s',
        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.2)',
    },
    loginContainer: {
        marginTop: '20px',
        textAlign: 'center',
    },
};

export default Main;
