import React, { useState, useEffect } from 'react';
import axios from 'axios';

const StoryAdd = () => {
    const [protagonist, setProtagonist] = useState('');
    const [characters, setCharacters] = useState('');
    const [storyOutline, setStoryOutline] = useState('');
    const [story, setStory] = useState(null);
    const [error, setError] = useState(null);
    const [token, setToken] = useState('');

    useEffect(() => {
        // 서버 환경과 로컬 환경을 분기하여 토큰 설정
        const savedToken = process.env.NODE_ENV === 'development'
            ? localStorage.getItem('token')
            : document.cookie.split('; ').find(row => row.startsWith('token=')).split('=')[1];
        
        if (!savedToken) {
            setError("토큰이 없습니다. 로그인을 다시 시도해주세요.");
        } else {
            setToken(savedToken);
            console.log('Retrieved Token:', savedToken); // 콘솔에 토큰 출력
        }
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        if (!token) {
            setError("유효한 토큰이 필요합니다. 로그인을 다시 시도해주세요.");
            return;
        }

        try {
            const response = await axios.post(
                'https://fairytalegenerator.kro.kr/api/tales/generate',
                {
                    protagonist,
                    characters,
                    storyOutline,
                },
                {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            setStory(response.data);
            console.log("Story generated successfully:", response.data);

        } catch (err) {
            if (err.response) {
                setError(`Error: ${err.response.data.message || '동화 생성에 실패했습니다.'}`);
                console.error("Error details:", err.response.data);
            } else if (err.request) {
                setError("서버 응답이 없습니다. 다시 시도해 주세요.");
                console.error("No response received:", err.request);
            } else {
                setError("요청을 처리하는 중 오류가 발생했습니다.");
                console.error("Error setting up request:", err.message);
            }
        }
    };

    return (
        <div style={styles.container}>
            <h1>📝 동화 생성하기</h1>
            <form onSubmit={handleSubmit} style={styles.form}>
                <input
                    type="text"
                    placeholder="주인공 (예: 작은 소녀)"
                    value={protagonist}
                    onChange={(e) => setProtagonist(e.target.value)}
                    style={styles.input}
                    required
                />
                <input
                    type="text"
                    placeholder="등장 인물 (예: 마법사, 용)"
                    value={characters}
                    onChange={(e) => setCharacters(e.target.value)}
                    style={styles.input}
                    required
                />
                <textarea
                    placeholder="스토리 줄거리 (예: 마법사에게 잡혀간 친구를 구하기 위해 모험을 떠나는 이야기)"
                    value={storyOutline}
                    onChange={(e) => setStoryOutline(e.target.value)}
                    style={styles.textarea}
                    required
                />
                <button type="submit" style={styles.button}>동화 생성</button>
            </form>
            
            {error && <p style={styles.error}>{error}</p>}

            {story && (
                <div style={styles.storyContainer}>
                    <h2>🌟 생성된 동화</h2>
                    <p>{story.content}</p>
                </div>
            )}
        </div>
    );
};

const styles = {
    container: {
        textAlign: 'center',
        padding: '20px',
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '10px',
        maxWidth: '500px',
        margin: 'auto',
    },
    input: {
        padding: '10px',
        width: '100%',
        borderRadius: '5px',
        border: '1px solid #ddd',
        fontSize: '1rem',
    },
    textarea: {
        padding: '10px',
        width: '100%',
        height: '100px',
        borderRadius: '5px',
        border: '1px solid #ddd',
        fontSize: '1rem',
    },
    button: {
        padding: '10px 20px',
        backgroundColor: '#4CAF50',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    error: {
        color: 'red',
        marginTop: '10px',
    },
    storyContainer: {
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#f9f9f9',
        borderRadius: '5px',
        boxShadow: '0px 4px 10px rgba(0, 0, 0, 0.1)',
    },
};

export default StoryAdd;
