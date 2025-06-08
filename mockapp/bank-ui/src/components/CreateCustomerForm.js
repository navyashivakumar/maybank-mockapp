import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import '../styles/Form.css';

function CreateCustomerForm({ token, onSuccess }) {
    const [name, setName] = useState('');
    const [createdCustomer, setCreatedCustomer] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!name.trim()) {
            toast.error('Name is required');
            return;
        }

        try {
            const res = await axios.post(
                'https://localhost:8080/customer/api/create',
                { name },
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            const customer = res.data;
            setCreatedCustomer(customer);
            toast.success(`Customer created with ID: ${customer.id}`);
            if (onSuccess) onSuccess(customer.id);
        } catch (error) {
            toast.error('Failed to create customer');
            console.error(error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="form">
            <input
                type="text"
                placeholder="Customer Name"
                value={name}
                onChange={e => setName(e.target.value)}
            />
            <button type="submit">Create</button>

            {createdCustomer && (
                <div className="result">
                    <p><strong>ID:</strong> {createdCustomer.id}</p>
                    <p><strong>Name:</strong> {createdCustomer.name}</p>
                </div>
            )}
        </form>
    );
}

export default CreateCustomerForm;
