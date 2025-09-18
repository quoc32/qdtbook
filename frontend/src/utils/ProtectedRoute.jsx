import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ element }) => {
  const sessionId = localStorage.getItem("sessionId");

  if (!sessionId) {
    // chưa đăng nhập → chuyển về login
    return <Navigate to="/login" replace />;
  }

  // có sessionId → cho vào trang
  return element;
};

export default ProtectedRoute;
