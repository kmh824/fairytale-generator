// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Main from './pages/mainpage';
import Home from './pages/Homepage';
import StoryAddPage from './pages/storyaddpage'; // storyaddpage 파일 불러오기
import Detail from './pages/detailpage';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Main />} />
                <Route path="/home" element={<Home />} />
                <Route path="/storyadd" element={<StoryAddPage />} /> {/* storyaddpage 경로 추가 */}
                <Route path="/detail" element={<Detail />} />
            </Routes>
        </Router>
    );
};

export default App;
