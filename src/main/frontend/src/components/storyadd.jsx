import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅 추가

const StoryAdd = () => {
    const [protagonist, setProtagonist] = useState('');
    const [characters, setCharacters] = useState('');
    const [storyOutline, setStoryOutline] = useState('');
    const [story, setStory] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [token, setToken] = useState('');
    const [saveStatus, setSaveStatus] = useState(null);
    const navigate = useNavigate(); // useNavigate 훅 초기화

    useEffect(() => {
        const savedToken = localStorage.getItem('token');
        if (!savedToken) {
            setError("토큰이 없습니다. 로그인을 다시 시도해주세요.");
        } else {
            setToken(savedToken);
        }
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setStory(null);
        setSaveStatus(null);
        setLoading(true);

        if (!token) {
            setError("유효한 토큰이 필요합니다. 로그인을 다시 시도해주세요.");
            setLoading(false);
            return;
        }

        try {
            const response = await axios.post(
                '/api/tales/generate',
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

            setStory(response.data); // API 응답 데이터 설정

        } catch (err) {
            if (err.response) {
                setError(`Error: ${err.response.data.message || '동화 생성에 실패했습니다.'}`);
            } else if (err.request) {
                setError("서버 응답이 없습니다. 다시 시도해 주세요.");
            } else {
                setError("요청을 처리하는 중 오류가 발생했습니다.");
            }
        } finally {
            setLoading(false);
        }
    };

    const saveStory = async () => {
        if (!story) {
            setError("저장할 동화가 없습니다.");
            return;
        }

        setLoading(true);
        setSaveStatus(null);
        setError(null);

        try {
            const savePromise = axios.post(
                '/api/fairy-tales/create',
                {
                    title: story.title || "제목 없음",
                    content: story.talePages ? story.talePages.join('\n') : "내용 없음",
                    imageUrls: story.illustrationUrls || [],
                },
                {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            const saveResponse = await savePromise;

            if (saveResponse.status === 200 || saveResponse.status === 201) {
                console.log("✅ 동화가 성공적으로 저장되었습니다!");
                // 콘솔에 저장된 내용 출력
                console.log("저장된 동화:", {
                    title: story.title || "제목 없음",
                    content: story.talePages ,
                    imageUrls: story.illustrationUrls,
                });} 
            else {
                setError("⚠️ 동화 저장에 실패했습니다. 다시 시도해주세요.");
            }
        } catch (err) {
            setError("동화 저장 실패: " + (err.response?.data?.message || "서버 오류"));
        } finally {
            setLoading(false);
        }
    };

    const handleGoToDetailPage = () => {
        navigate('/detail'); // 'detail' 페이지로 이동
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
                <button type="submit" style={styles.button} disabled={loading}>
                    {loading ? '생성 중...' : '동화 생성'}
                </button>
            </form>

            {loading && <p style={styles.loading}>⏳ 동화를 생성 중입니다. 잠시만 기다려 주세요...</p>}

            {error && <p style={styles.error}>{error}</p>}
            {saveStatus && <p style={styles.saveStatus}>{saveStatus}</p>}

            {story && (
                <div style={styles.storyContainer}>
                    <h2>🌟 {story.title}</h2>
                    {story.talePages && (
                        <div>
                            <h3>📖 동화 내용</h3>
                            {story.talePages.map((page, index) => (
                                <p key={index}>{page}</p>
                            ))}
                        </div>
                    )}
                    {story.illustrationUrls && (
                        <div>
                            {story.illustrationUrls.map((url, index) => (
                                <img
                                    key={index}
                                    src={url}
                                    alt={`Illustration ${index + 1}`}
                                    style={styles.image}
                                />
                            ))}
                        </div>
                    )}
                </div>
            )}

            {/* 저장하기 버튼 추가 */}
            {story && (
                <div>
                    <button
                        style={styles.saveButton}
                        onClick={saveStory}
                        disabled={loading}
                    >
                        {loading ? '저장 중...' : '저장하기'}
                    </button>

                    {/* 목록으로 버튼 추가 */}
                    <button
                        style={styles.saveButton}
                        onClick={handleGoToDetailPage}
                    >
                        목록으로
                    </button>
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
    saveButton: {
        padding: '10px 20px',
        backgroundColor: '#4CAF50',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        marginTop: '20px',
    },
    error: {
        color: 'red',
        marginTop: '10px',
    },
    loading: {
        marginTop: '10px',
        color: '#888',
        fontStyle: 'italic',
    },
    saveStatus: {
        color: 'green',
        marginTop: '10px',
        fontWeight: 'bold',
    },
    storyContainer: {
        marginTop: '20px',
        padding: '15px',
        backgroundColor: '#f9f9f9',
        borderRadius: '5px',
        boxShadow: '0px 4px 10px rgba(0, 0, 0, 0.1)',
    },
    image: {
        maxWidth: '100%',
        marginTop: '10px',
        borderRadius: '5px',
    },
};

export default StoryAdd;
