import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';

function DepositForm({ token, accountNumber: initialAccountNumber }) {
    const [accountNumber] = useState(initialAccountNumber); // Read-only
    const [amount, setAmount] = useState('');
    const [account, setAccount] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!accountNumber || !amount) return toast.error('Please enter amount');

        try {
            const res = await axios.put(
                `https://localhost:8080/account/api/deposit`,
                null,
                {
                    params: { accountNumber, amount },
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            setAccount(res.data);
            toast.success('Deposit successful!');
        } catch (err) {
            toast.error('Deposit failed');
        }
    };

    return (
        <form onSubmit={handleSubmit} className="form">
            <input type="text" value={accountNumber} readOnly />
            <input
                type="number"
                placeholder="Amount"
                value={amount}
                onChange={e => setAmount(e.target.value)}
            />
            <button type="submit">Deposit</button>
            {account && (
                <div className="result">
                    <p><strong>Account No:</strong> {account.accountNumber}</p>
                    <p><strong>Balance:</strong> {account.balance}</p>
                </div>
            )}
        </form>
    );
}

export default DepositForm;
