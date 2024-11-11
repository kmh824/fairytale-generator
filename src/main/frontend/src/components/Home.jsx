import React from 'react';

const Home = () => {
    return (
        <div style={styles.container}>
            {/* Hero Section */}
            <section style={styles.heroSection}>
                <h1 style={styles.title}>📖 동화 속 세상을 경험해보세요!</h1>
                <p style={styles.description}>
                    창의적이고 매력적인 이야기로 새로운 모험을 시작하세요. 어디서든 동화가 기다리고 있습니다.
                </p>
                <button style={styles.heroButton}>지금 스토리 생성하기</button>
            </section>

            {/* Gallery Section */}
            <section style={styles.gallerySection}>
               
                <div style={styles.galleryImages}>
                    <img src="" alt="스크 1" style={styles.galleryImage} />
                    <img src="" alt="스크 2" style={styles.galleryImage} />
                    <img src="" alt="스크 3" style={styles.galleryImage} />
                    <img src="" alt="스크 4" style={styles.galleryImage} />
                    {/* 추가 이미지를 여기에 추가할 수 있습니다. */}
                </div>
            </section>
        </div>
    );
};

// 스타일 정의
const styles = {
    container: {
        fontFamily: 'Arial, sans-serif',
    },
    heroSection: {
        textAlign: 'center',
        padding: '60px 20px',
        backgroundColor: '#f5f5f5',
    },
    title: {
        fontSize: '2.5rem',
        color: '#333',
        marginBottom: '10px',
    },
    description: {
        fontSize: '1.2rem',
        color: '#666',
        marginBottom: '20px',
    },
    heroButton: {
        fontSize: '1.2rem',
        color: '#fff',
        backgroundColor: '#4CAF50',
        padding: '10px 30px',
        borderRadius: '5px',
        border: 'none',
        cursor: 'pointer',
    },
    gallerySection: {
        padding: '40px 20px',
        backgroundColor: '#f9f9f9',
        textAlign: 'center',
        minHeight: '60vh',
    },
    galleryTitle: {
        fontSize: '2rem',
        marginBottom: '20px',
        color: '#333',
    },
    galleryImages: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', // 바둑판 형식으로 열 개수 설정
        gap: '10px', // 이미지 간의 간격
        justifyItems: 'center', // 아이템 정렬
    },
    galleryImage: {
        maxWidth: '100%',
        height: 'auto',
        borderRadius: '8px',
        boxShadow: '0px 4px 10px rgba(0, 0, 0, 0.1)',
    },
};

export default Home;
