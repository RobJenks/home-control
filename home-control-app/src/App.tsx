import 'App.css'
import React from 'react';
import axios, * as Axios from "axios"
import { QueryClient, QueryClientProvider, useQuery } from "react-query";
import * as Events from 'types/events';
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

 