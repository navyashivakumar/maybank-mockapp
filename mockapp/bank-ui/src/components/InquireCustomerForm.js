import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import '../styles/Form.css'; // Add this line

function InquireCustomerForm({ token, onSuccess }) {
    const [id, setId] = useState('');
    const [customer, setCustomer] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!id) return toast.error('Enter customer ID');

        try {
            const res = await axios.get(
                `https://localhost:8080/customer/api/customers/${id}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            setCustomer(res.data);
            toast.success('Customer found');
            if (onSuccess) onSuccess(res.data.id); // pass ID back to App
        } catch (error) {
            setCustomer(null);
            toast.error('Customer not found');
            console.error(error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="form">
            <input
                type="number"
                placeholder="Customer ID"
                value={id}
                onChange={e => setId(e.target.value)}
            />
            <button type="submit">Search</button>

            {customer && (
                <div className="result">
                    <p><strong>ID:</strong> {customer.id}</p>
                    <p><strong>Name:</strong> {customer.name}</p>
                </div>
            )}
        </form>
    );
}
export default InquireCustomerForm;
