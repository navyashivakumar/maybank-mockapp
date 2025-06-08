import React, { useState } from 'react';

function CollapsibleSection({ title, children }) {
    const [open, setOpen] = useState(false);

    return (
        <div className="collapsible-section">
            <div
                className="collapsible-header"
                onClick={() => setOpen(!open)}
                style={{
                    cursor: 'pointer',
                    borderBottom: '2px solid #ccc',
                    paddingBottom: '6px',
                    marginBottom: '8px',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    fontWeight: 'bold'
                }}
            >
                <span>{title}</span>
                <span>{open ? '▲' : '▼'}</span>
            </div>
            {open && <div className="collapsible-content">{children}</div>}
        </div>
    );
}

export default CollapsibleSection;
