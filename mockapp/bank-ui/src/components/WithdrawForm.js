import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';

function WithdrawForm({ token, accountNumber }) {
    const [amount, setAmount] = useState('');
    const [account, setAccount] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!amount) return toast.error('Enter amount');

        try {
            const res = await axios.put(
                'https://localhost:8080/account/api/withdraw',
                null,
                {
                    params: { accountNumber, amount },
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            setAccount(res.data);
            toast.success('Withdrawal successful!');
        } catch (err) {
            toast.error('Withdrawal failed');
            console.error(err);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="form">
            <input
                type="number"
                placeholder="Amount"
                value={amount}
                onChange={e => setAmount(e.target.value)}
            />
            <button type="submit">Withdraw</button>
            {account && (
                <div className="result">
                    <p><strong>Account No:</strong> {account.accountNumber}</p>
                    <p><strong>Balance:</strong> {account.balance}</p>
                </div>
            )}
        </form>
    );
}

export default WithdrawForm;
