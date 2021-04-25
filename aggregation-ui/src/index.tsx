import React from 'react';
import ReactDOM from 'react-dom';
import 'index.css';
import App from 'App';
import reportWebVitals from 'reportWebVitals';
import { SplitView } from 'components/util/splitView'
import { SplitViewVertical } from 'components/util/splitViewVertical'

declare global {
  interface Window { config: any; }
}

ReactDOM.render(
  <React.StrictMode>    
      <div style={{ height: "100%" }}>
        {/* <SplitView
          left={<div style={{ margin: "1rem" }}><App /></div>}
          right={<div style={{ margin: "1rem" }}><App /></div>}
        />
      </div> */}
      <SplitViewVertical
          top={<div style={{ height: "100%", boxSizing: "border-box" }}><App /></div>}
          bottom={<div style={{ height: "100%", boxSizing: "border-box" }}><App /></div>}
        />
      </div>
  </React.StrictMode>,
  document.getElementById('root')
);

reportWebVitals();
