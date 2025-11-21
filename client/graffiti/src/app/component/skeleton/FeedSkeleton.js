import React from 'react';
import { Skeleton, Box, Stack } from '@mui/material';

export default function FeedSkeleton() {
  return (
    <Box sx={{ padding: 2, maxWidth: 600, margin: 'auto' }}>
      {/* 1. 헤더 (프로필 이미지와 닉네임 영역) */}
      <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: 2 }}>
        {/* 프로필 이미지 스켈레톤 (원형) */}
        <Skeleton variant="circular" width={40} height={40} sx={{ marginRight: 1 }} />
        <Stack spacing={1}>
          {/* 닉네임 스켈레톤 */}
          <Skeleton variant="text" sx={{ fontSize: '1rem', width: 100 }} />
          {/* 생성 시간 스켈레톤 */}
          <Skeleton variant="text" sx={{ fontSize: '0.75rem', width: 80 }} />
        </Stack>
      </Box>

      {/* 2. 이미지 영역 스켈레톤 */}
      <Skeleton variant="rectangular" width="100%" height={250} sx={{ marginBottom: 2 }} />

      {/* 3. 본문 내용 스켈레톤 */}
      <Stack spacing={1} sx={{ marginBottom: 3 }}>
        <Skeleton variant="text" sx={{ fontSize: '1.2rem' }} />
        <Skeleton variant="text" sx={{ fontSize: '1.2rem', width: '90%' }} />
        <Skeleton variant="text" sx={{ fontSize: '1.2rem', width: '70%' }} />
      </Stack>
      
      {/* 4. 댓글 입력 필드 스켈레톤 */}
      <Skeleton variant="rectangular" width="100%" height={50} sx={{ marginBottom: 2 }} />

      {/* 5. 댓글 목록 스켈레톤 (예시 2개) */}
      <Stack spacing={2}>
         <Skeleton variant="text" sx={{ fontSize: '1rem', width: '80%' }} />
         <Skeleton variant="text" sx={{ fontSize: '1rem', width: '95%' }} />
      </Stack>
    </Box>
  );
}