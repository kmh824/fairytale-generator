import React, { useEffect, useState } from 'react';

const Main = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const token = params.get('token');

        if (token) {
            localStorage.setItem('token', token);
            setIsLoggedIn(true);
            if (window.location.hostname === 'localhost') {
                window.location.href = '/home'; // 로컬 환경 리디렉션
            } else {
                window.location.href = 'https://fairytalegenerator.kro.kr/home'; // 서버 환경 리디렉션
            }
        } else {
            const authToken = localStorage.getItem('token');
            if (authToken) {
                setIsLoggedIn(true);
            }
        }
    }, []);

    const handleLoginClick = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google'; // 로그인 URL로 리디렉션
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
