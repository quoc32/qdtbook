import { BrowserRouter, Routes, Route } from "react-router-dom";
import routes from "./utils/routes";

const App = () => {
  return ( 
    <BrowserRouter>
      
      <Routes>
        {routes.map((r, index) => (
          <Route key={index} path={r.path} element={r.element} />
        ))}
      </Routes>

    </BrowserRouter>
  );
}
 
export default App;