import React from 'react';

const Home = ({ isLoggedIn, handleLoginClick }) => {
    return (
        <div>
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

export default Home;
