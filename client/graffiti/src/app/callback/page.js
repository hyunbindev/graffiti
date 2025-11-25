import { Suspense } from 'react';
import AuthenticationCallback from './AuthenticationCallback'; 

export default function CallbackPage() {
  return (
    // 서버 측 렌더링 시 useSearchParams를 사용하는 컴포넌트의 실행을 지연시킵니다.
    <Suspense fallback={
        <div style={{ padding: '40px', textAlign: 'center' }}>
            로그인 정보 확인 중...
        </div>
    }>
      {/* ⬇️ useSearchParams 훅을 포함한 실제 로직을 담은 컴포넌트 */}
      <AuthenticationCallback />
    </Suspense>
  );
}