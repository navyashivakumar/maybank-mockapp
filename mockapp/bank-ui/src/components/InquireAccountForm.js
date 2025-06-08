import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';

function InquireAccountForm({ token, onSuccess, setCurrentStep }) {
    const [accountNumber, setAccountNumber] = useState('');
    const [account, setAccount] = useState(null);
    const [showModal, setShowModal] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!accountNumber) return toast.error('Enter account number');

        try {
            const res = await axios.get(
                `https://localhost:8080/account/api/accounts/${accountNumber}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setAccount(res.data);
            setShowModal(true);
            toast.success('Account found!');
        } catch (error) {
            setAccount(null);
            toast.error('Account not found');
            console.error(error);
        }
    };

    const closeModal = () => {
        setShowModal(false);
        setAccount(null);
    };

    const handleGoToAccount = () => {
        if (onSuccess && account) onSuccess(account.accountNumber);
        if (setCurrentStep) setCurrentStep(3);
        closeModal();
    };

    return (
        <>
            <form onSubmit={handleSubmit} className="form">
                <input
                    type="number"
                    placeholder="Account Number"
                    value={accountNumber}
                    onChange={(e) => setAccountNumber(e.target.value)}
                />
                <button type="submit">Find Account</button>
            </form>

            {showModal && account && (
                <div className="modal-overlay" style={modalOverlayStyle}>
                    <div className="modal" style={modalStyle}>
                        <button onClick={closeModal} style={closeButtonStyle}>
                            &times;
                        </button>
                        <h2>Account Details</h2>
                        <p><strong>Account No:</strong> {account.accountNumber}</p>
                        <p><strong>Status:</strong> {account.status}</p>
                        <p><strong>Type:</strong> {account.accountType}</p>
                        <p><strong>Balance:</strong> {account.balance}</p>
                        <p><strong>Customer ID:</strong> {account.customerId}</p>

                        {account.status !== 'Closed' && (
                            <button onClick={handleGoToAccount} style={goToAccountButtonStyle}>
                                Go to Account
                            </button>
                        )}
                    </div>
                </div>
            )}
        </>
    );
}


const modalOverlayStyle = {
    position: 'fixed',
    top: 0, left: 0, right: 0, bottom: 0,
    backgroundColor: 'rgba(0,0,0,0.5)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 1000,
};

const modalStyle = {
    backgroundColor: '#fff',
    padding: '20px',
    borderRadius: '8px',
    width: '300px',
    position: 'relative',
    boxShadow: '0 2px 8px rgba(0,0,0,0.3)',
};

const closeButtonStyle = {
    position: 'absolute',
    top: '10px',
    right: '10px',
    border: 'none',
    background: 'none',
    fontSize: '20px',
    cursor: 'pointer',
};

const goToAccountButtonStyle = {
    display: 'block',
    margin: '15px auto 0', // top: 15px, left/right: auto (centers), bottom: 0
    padding: '8px 12px',
    backgroundColor: '#000',
    color: '#ffd700',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
};


export default InquireAccountForm;
