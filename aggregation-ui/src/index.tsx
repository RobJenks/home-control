import React from 'react';
import ReactDOM from 'react-dom';
import 'index.css';
import App from 'App';
import reportWebVitals from 'reportWebVitals';
import { SplitView } from 'components/util/splitView'

declare global {
  interface Window { config: any; }
}

ReactDOM.render(
  <React.StrictMode>    
      <div>
        <SplitView
          left={<div style={{ margin: "1rem" }}><App /></div>}
          right={<div style={{ margin: "1rem" }}><App /></div>}
        />
      </div>
  </React.StrictMode>,
  document.getElementById('root')
);

reportWebVitals();
