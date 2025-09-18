import { BrowserRouter, Routes, Route } from "react-router-dom";
import routes from "./utils/routes";
import ProtectedRoute from "./utils/ProtectedRoute";

const App = () => {
  return ( 
    <BrowserRouter>
      <Routes>
        {routes.map((r, index) => (
          <Route
            key={index}
            path={r.path}
            element={
              r.protected 
                ? <ProtectedRoute element={r.element} /> 
                : r.element
            }
          />
        ))}
      </Routes>
    </BrowserRouter>
  );
}
 
export default App;
