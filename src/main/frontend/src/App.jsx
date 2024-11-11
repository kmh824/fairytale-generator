// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Main from './pages/mainpage';
import Home from './pages/Homepage';
import StoryAddPage from './pages/storyaddpage'; // storyaddpage 파일 불러오기

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Main />} />
                <Route path="/home" element={<Home />} />
                <Route path="/storyadd" element={<StoryAddPage />} /> {/* storyaddpage 경로 추가 */}
            </Routes>
        </Router>
    );
};

export default App;
