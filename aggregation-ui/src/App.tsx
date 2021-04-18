import './App.css'
import React from 'react';
import axios, * as Axios from "axios"
import { QueryClient, QueryClientProvider, useQuery } from "react-query";

declare global {
  interface Window { config: any; }
}

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <DoThings />
    </QueryClientProvider>
  )
}

type int = number;
type EventResponseData = {
  count: int,
  data: EventData[]
}
type EventData = {
  key: string,
  offset: int,
  value: Object[]
}

// https://blog.theodo.com/2020/11/react-resizeable-split-panels/
// https://codesandbox.io/s/splitview-p37yw?file=/src/components/SplitView.tsx

function DoThings() : JSX.Element {
  const [pollIntervalMs, ] = React.useState(2000);
  const targetUrl = window.config.aggregationServiceUrl + "/updates?count=40";

  // eslint-disable-next-line no-unused-vars
  const { data, error } = 
    useQuery<Axios.AxiosResponse<EventResponseData>, Error>("testDataQuery", 
    async () => axios.get<EventResponseData>(targetUrl),
    {
      refetchInterval: pollIntervalMs
    }
  );

  if (error) {
    var err = "Query error: " + error.message;
    console.error(err);
    return (<p>{err}</p>);
  }

  return (
    <div className="App">
      <div>
        <table className="eventTable">
          <thead>
            <tr>
              <th>#</th>
              <th>Event</th>
            </tr>
          </thead>
          
          <tbody>
            {cleanData(data).map((ev, i) => {
              return (
                <tr key={ev.offset}>
                  <td>{ev.offset}</td>
                  <td>{JSON.stringify(ev)}</td>
                </tr>
              )
            })}
          </tbody>
        </table>
      </div>
      
    </div>
  );
}

function cleanData(queryResponse?: Axios.AxiosResponse<EventResponseData>) : EventData[] {
  if (queryResponse) {
    if (queryResponse.data) {
      return queryResponse.data.data;
    }
  }

  return [];
}


export default App;

 