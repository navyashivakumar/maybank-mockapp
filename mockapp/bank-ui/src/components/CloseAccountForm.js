import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';

function CloseAccountForm({ token, accountNumber }) {
    const [account, setAccount] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!accountNumber) return toast.error('Account number is required');

        try {
            const res = await axios.put(
                'https://localhost:8080/account/api/close',
                null,
                {
                    params: { accountNumber },
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            setAccount(res.data);
            toast.success('Account closed successfully');
        } catch (err) {
            toast.error('Failed to close account');
            console.error(err);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="form">
            <button type="submit">Close Account</button>
            {account && (
                <div className="result">
                    <p><strong>Account No:</strong> {account.accountNumber}</p>
                    <p><strong>Status:</strong> {account.status}</p>
                </div>
            )}
        </form>
    );
}

export default CloseAccountForm;
