import 'App.css'
import React from 'react';
import axios, * as Axios from "axios"
import { QueryClient, QueryClientProvider, useQuery } from "react-query";
import * as Events from 'types/events';
import * as State from 'types/state';
import { SplitView } from 'components/util/splitView'
import { SplitViewVertical } from 'components/util/splitViewVertical'
import PrimaryDisplay from 'components/display/primaryDisplay'
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
              {<SplitView initialSplitPosition={800} 
                left={ PrimaryDisplayPane() }
                
                right=
                {<SplitViewVertical initialSplitPosition={800}
                  top={<img src="assets/img/logo192.png" alt="placeholder" />}
                  bottom={<img src="assets/img/logo192.png" alt="placeholder"/>}                
                />}

              />}

            bottom={ UpdatesPane() } 
        />}
      </div>
  )
}

function PrimaryDisplayPane() : JSX.Element {
  const [statePollIntervalMs, ] = React.useState(3000);
  const targetUrl = window.config.aggregationServiceUrl + "/state";

  const { data, error } = 
    useQuery<Axios.AxiosResponse<State.HomeState>, Error>("stateQuery", 
    async () => axios.get<State.HomeState>(targetUrl),
    {
      refetchInterval: statePollIntervalMs
    }
  );

  if (error) {
    var err = "Query error: " + error.message;
    console.error(err);
    return (<p>{err}</p>);
  }

  return (<PrimaryDisplay state={data?.data} />);
}

function UpdatesPane() : JSX.Element {
  const [updatesPollIntervalMs, ] = React.useState(2000);
  const targetUrl = window.config.aggregationServiceUrl + "/updates?count=40";

  const { data, error } = 
    useQuery<Axios.AxiosResponse<Events.ResponseData>, Error>("updateQuery", 
    async () => axios.get<Events.ResponseData>(targetUrl),
    {
      refetchInterval: updatesPollIntervalMs
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

 