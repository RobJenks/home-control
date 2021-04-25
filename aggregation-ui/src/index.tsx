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
      <div style={{ width: "100%", height: "100%" }}>
        {/* {<SplitView
            left=
              {<SplitViewVertical
                top={<App />}
                bottom=
                
                {<SplitView
                  left={<App />}
                  right={<App />}                
                />}

              />}

            right={<App />} 
        />} */}


        {<SplitViewVertical
            top=
              {<SplitView
                left={<App />}
                right=
                
                {<SplitViewVertical
                  top={<App />}
                  bottom={<App />}                
                />}

              />}

            bottom={<App />} 
        />}


      </div>
      
  </React.StrictMode>,
  document.getElementById('root')
);

reportWebVitals();
