import React, { useState, useRef, useEffect } from 'react';
import DarkModeToggle from './DarkModeToggle';
import '../styles/AccountMenu.css';

function AccountMenu({ onLogout, username }) {
    const [open, setOpen] = useState(false);
    const menuRef = useRef();

    const handleClickOutside = (e) => {
        if (menuRef.current && !menuRef.current.contains(e.target)) {
            setOpen(false);
        }
    };

    useEffect(() => {
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    return (
        <div className="account-menu" ref={menuRef}>
            <button className="account-button" onClick={() => setOpen(!open)}>
                👤
            </button>
            {open && (
                <div className="dropdown">
                    <div className="dropdown-item"> {username}</div>
                    <div className="dropdown-item">
                        <DarkModeToggle />
                    </div>
                    <div className="dropdown-item logout" onClick={onLogout}>
                        🚪 Logout
                    </div>
                </div>
            )}
        </div>
    );
}

export default AccountMenu;
