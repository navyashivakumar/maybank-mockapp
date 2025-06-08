import React, { useState } from 'react';
import './styles/App.css';
import CreateCustomerForm from './components/CreateCustomerForm';
import InquireCustomerForm from './components/InquireCustomerForm';
import CreateAccountForm from './components/CreateAccountForm';
import InquireAccountForm from './components/InquireAccountForm';
import DepositForm from './components/DepositForm';
import WithdrawForm from './components/WithdrawForm';
import CloseAccountForm from './components/CloseAccountForm';
import LoginPage from './components/LoginPage';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import AccountMenu from "./components/AccountMenu";
import CollapsibleSection from "./components/CollapsibleSection";

function App() {
    const [token, setToken] = useState(localStorage.getItem('token') || '');
    const [currentStep, setCurrentStep] = useState(1);
    const [customerId, setCustomerId] = useState(null);
    const [accountNumber, setAccountNumber] = useState(null);
    const [username, setUsername] = useState('');

    const handleLogout = () => {
        localStorage.removeItem('token');
        setToken('');
        setUsername('');
        setCurrentStep(1);
        setCustomerId(null);
        setAccountNumber(null);
    };

    return (
        <div className="app-container">
            <div className="header">
                <div className="header-icon">
                    <img src="/MaybankLogo.jpeg" alt="Tiger Logo"/>
                </div>
                <h1>Maybank Banking Portal</h1>
                {token && <AccountMenu onLogout={handleLogout} username={username}/>}
            </div>

            {!token ? (
                <StepCard title="🔐 Login to continue">
                    <LoginPage onLogin={(t, u) => {
                        setToken(t);
                        setUsername(u);
                    }}/>

                </StepCard>
            ) : (
                <>
                    {currentStep === 1 && (
                        <StepCard title="👤 Customer Onboarding">
                            <div>
                                <h3>Create Customer</h3>
                                <CreateCustomerForm token={token} onSuccess={(id) => {
                                    setCustomerId(id);
                                    setCurrentStep(2);
                                }}/>
                            </div>
                            <div>
                                <h3>Inquire Customer</h3>
                                <InquireCustomerForm token={token} onSuccess={(id) => {
                                    setCustomerId(id);
                                    setCurrentStep(2);
                                }}/>
                            </div>
                        </StepCard>
                    )}

                    {currentStep === 2 && (
                        <StepCard title={`🏦 Account Management (Customer ID: ${customerId})`}
                                  onBack={() => setCurrentStep(1)}>
                            <div>
                                <h3>Create Account</h3>
                                <CreateAccountForm token={token} customerId={customerId} onSuccess={(accNo) => {
                                    setAccountNumber(accNo);
                                    setCurrentStep(3);
                                }}/>
                            </div>
                            <div>
                                <h3>Inquire Account</h3>
                                <InquireAccountForm token={token} onSuccess={(accNo) => {
                                    setAccountNumber(accNo);
                                    setCurrentStep(3);

                                }}/>
                            </div>
                        </StepCard>
                    )}

                    {currentStep === 3 && (
                        <StepCard
                            title={`💳 Account Transactions (Account No: ${accountNumber})`}
                            onBack={() => setCurrentStep(2)}
                        >
                            <div className="vertical-sections">
                                <CollapsibleSection
                                    title="Deposit"
                                    children={<DepositForm token={token} accountNumber={accountNumber}/>}
                                />
                                <CollapsibleSection
                                    title="Withdrawal"
                                    children={<WithdrawForm token={token} accountNumber={accountNumber}/>}
                                />
                                <CollapsibleSection
                                    title="Close Account"
                                    children={<CloseAccountForm token={token} accountNumber={accountNumber}/>}
                                />
                            </div>
                        </StepCard>
                    )}


                </>
            )}

            <ToastContainer position="top-right" autoClose={3000}/>
        </div>
    );


}

function StepCard({title, children, onBack}) {
    return (
        <section className="step-card">
            {onBack && (
                <button className="back-button" onClick={onBack}>
                    ←
                </button>
            )}
            <h2>{title}</h2>
            <div className="step-card-content">{children}</div>
        </section>
    );
}

export default App;
