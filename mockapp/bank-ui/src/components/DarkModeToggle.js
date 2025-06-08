import { useEffect, useState } from 'react';

function DarkModeToggle() {
    const [darkMode, setDarkMode] = useState(() => {
        return localStorage.getItem("darkMode") === "true";
    });

    useEffect(() => {
        document.body.classList.toggle('dark', darkMode);
        localStorage.setItem("darkMode", darkMode);
    }, [darkMode]);

    return (
        <div onClick={() => setDarkMode(!darkMode)} style={{ textAlign: 'center', cursor: 'pointer' }}>
            {darkMode ? '🌞 Light Mode' : '🌙 Dark Mode'}
        </div>
    );
}

export default DarkModeToggle;
