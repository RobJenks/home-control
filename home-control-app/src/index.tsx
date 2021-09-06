import React from 'react';
import ReactDOM from 'react-dom';
import 'index.css';
import App from 'App';
import reportWebVitals from 'reportWebVitals';

declare global {
  interface Window { config: any; }
}

ReactDOM.render(
  <React.StrictMode>    
      <div style={{ width: "100%", height: "100%" }}>
        { <App/> }
      </div>
      
  </React.StrictMode>,
  document.getElementById('root')
);

reportWebVitals();
