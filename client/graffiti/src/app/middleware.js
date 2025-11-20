import { NextResponse } from 'next/server';

// 1. 제외할 경로 (로그인, 회원가입, API 등)를 명시적으로 정의합니다.
const PUBLIC_PATHS = [
    '/login', 
    '/signup', 
    '/api', // API 경로는 보통 백엔드에서 자체적으로 인증 처리가 필요합니다.
    '/_next', // Next.js 내부 파일 (필수)
    '/static', // 정적 파일 경로 (필수)
    '/favicon.ico', // 파비콘 (필수)
];
const LOGIN_PAGE = '/login'; 

export function middleware(request) {
  const url = request.nextUrl;
  const path = url.pathname;

  // 2. 현재 경로가 public_paths로 시작하는지 확인합니다.
  // 즉, 인증 없이 접근이 허용된 페이지인지 확인합니다.
  const isPublicPath = PUBLIC_PATHS.some(p => path.startsWith(p));
  
  // 3. JWT 토큰을 쿠키에서 확인합니다. (토큰이 쿠키에 저장되어 있다고 가정)
  // 'access-token'은 예시 쿠키 이름입니다. 실제 사용하는 이름으로 변경하세요.
  const accessToken = request.cookies.get('access-token'); 
  const isAuthenticated = !!accessToken; // 토큰 존재 여부로 인증 상태 판단

  // 4. 인가 로직 실행
  
  // (1) Public Path가 아니고 (보호해야 하는 페이지),
  // (2) 인증되지 않았다면 (토큰이 없다면)
  if (!isPublicPath && !isAuthenticated) {
    
    // 로그인 페이지로 리디렉션합니다.
    const redirectUrl = new URL(LOGIN_PAGE, url.origin);
    
    // 사용자가 로그인 후 원래 페이지로 돌아올 수 있도록 현재 경로를 저장합니다.
    redirectUrl.searchParams.set('from', path); 
    
    return NextResponse.redirect(redirectUrl);
  }

  // (1) 인증이 되었거나, (2) Public Path라면 요청을 그대로 진행합니다.
  return NextResponse.next();
}

// 5. 미들웨어를 모든 요청에 적용합니다.
export const config = {
    // '/'로 시작하는 모든 경로를 매칭합니다.
    // 하지만 middleware 함수 내부의 PUBLIC_PATHS로 필터링되므로, 로그인/정적 파일 등은 통과합니다.
  matcher: [
    '/:path*',
  ],
};