// React 컴포넌트 상단에 이미지 import
import React from 'react';
import { useNavigate } from 'react-router-dom'; // React Router의 useNavigate 훅 사용
import Image1 from '../assets/image/1.png'; 
import Image2 from '../assets/image/2.png'; 
import Image3 from '../assets/image/3.png'; 
import Image4 from '../assets/image/4.png'; 
import Image5 from '../assets/image/5.png'; 
import Image6 from '../assets/image/6.png'; 
import Image7 from '../assets/image/7.png'; 
import Image8 from '../assets/image/8.png'; 

const Home = () => {
    const navigate = useNavigate(); // useNavigate 훅 초기화

    // '스토리 생성하기' 버튼 클릭 시 '/storyadd' 페이지로 이동
    const handleNavigateToStoryAdd = () => {
        navigate('/storyadd');
    };

    // '동화 목록 조회하기' 버튼 클릭 시 '/detailpage' 페이지로 이동
    const handleNavigateToDetailPage = () => {
        navigate('/detail');
    };

    return (
        <div style={styles.container}>
            {/* Hero Section */}
            <section style={styles.heroSection}>
                <h1 style={styles.title}>📖 동화 속 세상을 경험해보세요!</h1>
                <p style={styles.description}>
                    창의적이고 매력적인 이야기로 새로운 모험을 시작하세요. 어디서든 동화가 기다리고 있습니다.
                </p>
                <div style={styles.buttonContainer}>
                    <button style={styles.heroButton} onClick={handleNavigateToStoryAdd}>
                        지금 스토리 생성하기
                    </button>
                    <button style={styles.heroButton} onClick={handleNavigateToDetailPage}>
                        동화 목록 조회하기
                    </button>
                </div>
            </section>

            {/* Gallery Section */}
            <section style={styles.gallerySection}>
                <div style={styles.galleryImages}>

                    <img src={Image1} alt="스크 1" style={styles.galleryImage} />
                    <img src={Image2} alt="스크 2" style={styles.galleryImage} />
                    <img src={Image3} alt="스크 3" style={styles.galleryImage} />
                    <img src={Image4} alt="스크 4" style={styles.galleryImage} />
                    <img src={Image5} alt="스크 5" style={styles.galleryImage} />
                    <img src={Image6} alt="스크 6" style={styles.galleryImage} />
                    <img src={Image7} alt="스크 7" style={styles.galleryImage} />
                    <img src={Image8} alt="스크 8" style={styles.galleryImage} />

                    
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
    buttonContainer: {
        display: 'flex',
        justifyContent: 'center',
        gap: '20px',
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
