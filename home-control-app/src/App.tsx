import 'App.css'
import React from 'react';
import axios, * as Axios from "axios"
import { QueryClient, QueryClientProvider, useQuery } from "react-query";
import * as Events from 'types/events';
import { SplitView } from 'components/util/splitView'
import { SplitViewVertical } from 'components/util/splitViewVertical'
import EventDataStream from 'components/events/eventDataStream'

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>      
      <RenderApp />
    </QueryClientProvider>
  )
}

function RenderApp() : JSX.Element {
  return PrimaryWindowPanes();
}

function PrimaryWindowPanes() : JSX.Element {
  return (
    <div style={{ width: "100%", height: "100%" }}>
        {<SplitViewVertical
            top=
              {<SplitView
                left={<img src="logo192.png" alt="placeholder"/>}
                right=
                
                {<SplitViewVertical
                  top={<img src="logo192.png" alt="placeholder" />}
                  bottom={<img src="logo192.png" alt="placeholder" />}                
                />}

              />}

            bottom={ UpdatesPane() } 
        />}
      </div>
  )
}

function UpdatesPane() : JSX.Element {
  const [pollIntervalMs, ] = React.useState(2000);
  const targetUrl = window.config.aggregationServiceUrl + "/updates?count=40";

  const { data, error } = 
    useQuery<Axios.AxiosResponse<Events.ResponseData>, Error>("testDataQuery", 
    async () => axios.get<Events.ResponseData>(targetUrl),
    {
      refetchInterval: pollIntervalMs
    }
  );

  if (error) {
    var err = "Query error: " + error.message;
    console.error(err);
    return (<p>{err}</p>);
  }

  var records = cleanData(data);
  return (<EventDataStream data={records} />);
}



function cleanData(queryResponse?: Axios.AxiosResponse<Events.ResponseData>) : Events.Data[] {
  if (queryResponse) {
    if (queryResponse.data) {
      return queryResponse.data.data;
    }
  }

  return [];
}


export default App;

 