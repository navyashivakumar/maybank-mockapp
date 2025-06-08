import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';

function CreateAccountForm({ token, customerId, onSuccess }) {
    const [type, setType] = useState('');
    const [account, setAccount] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!type) return toast.error('Select account type');

        try {
            const res = await axios.post(
                'https://localhost:8080/account/api/accounts',
                null,
                {
                    params: { customerId, type },
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            const acc = res.data;
            setAccount(acc);
            toast.success('Account created!');
            if (onSuccess) onSuccess(acc.accountNumber);
        } catch (error) {
            toast.error('Failed to create account');
            console.error(error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="form">
            <input type="text" placeholder="Account Type" value={type} onChange={e => setType(e.target.value)} />
            <button type="submit">Create</button>
            {account && (
                <div className="result">
                    <p><strong>Account No:</strong> {account.accountNumber}</p>
                    <p><strong>Status:</strong> {account.status}</p>
                    <p><strong>Type:</strong> {account.accountType}</p>
                    <p><strong>Balance:</strong> {account.balance}</p>
                    <p><strong>Customer ID:</strong> {account.customerId}</p>
                </div>
            )}
        </form>
    );
}

export default CreateAccountForm;
