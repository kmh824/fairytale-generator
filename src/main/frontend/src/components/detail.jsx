import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Detail = () => {
    const [stories, setStories] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchStories = async () => {
            setLoading(true);
            setError(null);
            try {
                const token = localStorage.getItem('token');
                if (!token) {
                    throw new Error("토큰이 없습니다. 로그인을 다시 시도해주세요.");
                }
                const response = await axios.get('/api/fairy-tales/user', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setStories(response.data); // 서버에서 받은 동화 데이터를 설정
            } catch (err) {
                if (err.response) {
                    setError(`Error: ${err.response.data.message || '동화를 가져오는 데 실패했습니다.'}`);
                } else {
                    setError("서버 응답이 없습니다. 다시 시도해 주세요.");
                }
            } finally {
                setLoading(false);
            }
        };

        fetchStories();
    }, []);

    const handleDelete = async (id) => {
        const token = localStorage.getItem('token');
        if (!token) {
            alert("토큰이 없습니다. 로그인을 다시 시도해주세요.");
            return;
        }

        try {
            await axios.delete(`/api/fairy-tales/delete/${id}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setStories(prevStories => prevStories.filter(story => story.id !== id));
            alert("동화가 삭제되었습니다.");
        } catch (err) {
            if (err.response) {
                alert(`Error: ${err.response.data.message || '동화를 삭제하는 데 실패했습니다.'}`);
            } else {
                alert("서버 응답이 없습니다. 다시 시도해 주세요.");
            }
        }
    };

    const handleEdit = async (id) => {
        const token = localStorage.getItem('token');
        if (!token) {
            alert("토큰이 없습니다. 로그인을 다시 시도해주세요.");
            return;
        }

        const newTitle = prompt("새로운 제목을 입력하세요:");
        if (!newTitle) return;

        try {
            const response = await axios.put(
                `/api/fairy-tales/edit/${id}`,
                { title: newTitle }, // 새로운 데이터
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            setStories(prevStories =>
                prevStories.map(story =>
                    story.id === id ? { ...story, title: response.data.title } : story
                )
            );
            alert("동화가 수정되었습니다.");
        } catch (err) {
            if (err.response) {
                alert(`Error: ${err.response.data.message || '동화를 수정하는 데 실패했습니다.'}`);
            } else {
                alert("서버 응답이 없습니다. 다시 시도해 주세요.");
            }
        }
    };

    return (
        <div style={styles.container}>
            <h1>📚 내 동화 조회</h1>
            {loading && <p style={styles.loading}>⏳ 동화를 불러오는 중입니다...</p>}
            {error && <p style={styles.error}>{error}</p>}
            {!loading && !error && stories.length === 0 && <p style={styles.noData}>동화가 없습니다.</p>}

            {stories.map((story) => (
                <div key={story.id} style={styles.story}>
                    <h2>🌟 {story.title}</h2>
                    <p>{story.talePages || "내용이 없습니다."}</p>
                    {story.illustrationUrls && story.illustrationUrls.length > 0 && (
                        <div style={styles.imageContainer}>
                            <img
                                src={story.illustrationUrls[0]} 
                                alt={story.title}
                                style={styles.image}
                            />
                        </div>
                    )}
                    <button onClick={() => handleDelete(story.id)} style={styles.deleteButton}>
                        삭제
                    </button>
                    <button onClick={() => handleEdit(story.id)} style={styles.editButton}>
                        수정
                    </button>
                </div>
            ))}
        </div>
    );
};

const styles = {
    container: {
        padding: '20px',
        textAlign: 'center',
    },
    loading: {
        color: '#888',
        fontStyle: 'italic',
    },
    error: {
        color: 'red',
    },
    noData: {
        marginTop: '20px',
        color: '#888',
    },
    story: {
        margin: '20px 0',
        padding: '15px',
        backgroundColor: '#f9f9f9',
        borderRadius: '5px',
        boxShadow: '0px 4px 10px rgba(0, 0, 0, 0.1)',
    },
    imageContainer: {
        marginTop: '15px',
    },
    image: {
        maxWidth: '100%',
        height: 'auto',
        borderRadius: '5px',
    },
    deleteButton: {
        marginTop: '15px',
        padding: '10px 20px',
        backgroundColor: '#ff4d4d',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        marginRight: '10px',
    },
    editButton: {
        marginTop: '15px',
        padding: '10px 20px',
        backgroundColor: '#4CAF50',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
    },
};

export default Detail;
